(ns app.api.routes
  (:require [app.api.response :refer [ok]]
            [app.api.indicators.handlers :as indicators]))

(defn health-check [_]
  (ok {:hello "world"}))

(defn temp [request]
  (let [datasource (get request :datasource)]
    (ok datasource)))

(def routes
  (set (concat ["/api/health-check" :get health-check :route-name ::health-check]
               ["/api/temp" :get temp :route-name ::temp]
               indicators/routes)))