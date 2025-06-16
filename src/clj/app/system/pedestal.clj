(ns app.system.pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.connector :as conn]
            [io.pedestal.http.http-kit :as hk]
            [app.api.routes :refer [routes]]))

(defn- inject-datasource
  [datasource]
  {:name ::inject-components
   :enter #(assoc-in % [:request :datasource] datasource)})

(defrecord Pedestal [config json-datasource]
  component/Lifecycle
  (start [this]
    (println "Starting Pedestal...")
    (println "Pedestal Server running on " (str (:host config) ":" (:port config)))
    (assoc this :server (-> (conn/default-connector-map (:host config) (:port config))
                            (conn/with-interceptor (inject-datasource json-datasource))
                            (conn/with-default-interceptors)
                            (conn/with-routes routes)
                            (hk/create-connector nil)
                            (conn/start!))))
  (stop [this]
    (println "Stopping Pedestal...")
    (conn/stop! (get this :server))
    nil))

(defn new-pedestal []
  (map->Pedestal {}))