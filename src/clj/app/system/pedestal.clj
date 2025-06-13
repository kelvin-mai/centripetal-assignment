(ns app.system.pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.connector :as conn]
            [io.pedestal.http.http-kit :as hk]
            [app.routes :refer [routes]]))

(defrecord Pedestal [config]
  component/Lifecycle
  (start [this]
    (assoc this ::pedestal (-> (conn/default-connector-map (:port config))
                               (conn/with-default-interceptors)
                               (conn/with-routes routes)
                               (hk/create-connector nil)
                               (conn/start!))))
  (stop [this]
    (println "Stopping Pedestal...")
    (conn/stop! (get this ::pedestal))
    (assoc this ::pedestal nil)))

(defn new-pedestal []
  (map->Pedestal {}))