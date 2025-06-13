(ns app.api.indicators.handlers
  (:require [app.api.response :refer [ok not-found]]))

(defn get-indicators [request]
  (let [ds (get request :datasource)]
    (ok ds)))

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