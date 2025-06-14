(ns app.api.indicators.handlers
  (:require [app.api.response :refer [ok not-found]]
            [clojure.string :as str]))

(defn get-indicators [request]
  (let [ds (get request :datasource)
        query (get request :query-params)
        docs (cond->> ds
               (:type query)
               (filter #(some (fn [i] (= (str/lower-case (:type i))
                                         (str/lower-case (:type query))))
                              (:indicators %))))]
    (ok docs)))

(defn get-indicator-by-id [request]
  (let [ds (get request :datasource)
        id (get-in request [:path-params :id])
        doc (->> ds
                 (filter #(= id (:id %)))
                 (first))]
    (if doc
      (ok doc)
      (not-found))))

(def routes
  [["/api/indicators" :get get-indicators :route-name ::get-indicators]
   ["/api/indicators/:id" :get get-indicator-by-id :route-name ::get-indicator-by-id]])