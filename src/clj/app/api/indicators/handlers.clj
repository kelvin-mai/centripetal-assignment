(ns app.api.indicators.handlers
  (:require [malli.core :as m]
            [app.api.response :as response :refer [ok not-found]]
            [app.api.indicators.datasource :as db]
            [app.api.indicators.schema :as schema]))

(defn get-indicators [request]
  (let [ds (get request :datasource)
        query (get request :query-params)
        docs (cond-> ds
               (:type query)
               (db/find-docs-with-type (:type query)))]
    (ok docs)))

(defn get-indicator-by-id [request]
  (let [ds (get request :datasource)
        id (get-in request [:path-params :id])
        doc (db/find-by-id ds id)]
    (if doc
      (ok doc)
      (not-found))))

(defn search-indicators [request]
  (let [ds (get request :datasource)
        body (get request :json-params)
        valid? (m/validate schema/search-params body)]
    (if (not valid?)
      (response/malli-error schema/search-params body)
      (let [props (select-keys body [:tlp
                                     :author_name
                                     :name
                                     :description
                                     :tags])
            docs (reduce-kv
                  (fn [db k v]
                    (case k
                      (:tlp :author_name :name :description)
                      (db/docs-includes-string-kv db k v)

                      (:tags)
                      (db/docs-includes-tags db v)

                      :else
                      db))
                  ds
                  props)]
        (if (empty? docs)
          (not-found)
          (ok docs))))))

(def routes
  [["/api/indicators" :get get-indicators :route-name ::get-indicators]
   ["/api/indicators/search" :post search-indicators :route-name ::search-indicators]
   ["/api/indicators/:id" :get get-indicator-by-id :route-name ::get-indicator-by-id]])