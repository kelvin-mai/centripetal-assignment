(ns app.system.json-datasource
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]
            [cheshire.core :as json]))

(defrecord JsonDatasource [config]
  component/Lifecycle
  (start [_]
    (println "Starting Json Datasource...")
    (-> (slurp (io/resource "indicators.json"))
        (json/decode true)))
  (stop [_]
    (println "Stopping Json Datasource...")
    nil))

(defn new-json-datasource []
  (map->JsonDatasource {}))