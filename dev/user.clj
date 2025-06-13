(ns user
  (:require [com.stuartsierra.component :as component]
            [clj-reload.core]
            [app.system.core :as system]
            [io.pedestal.connector.test :as test]))

(defonce *system (atom nil))

(defn start! []
  (reset! *system (-> (system/new-system)
                      (component/start-system))))

(defn stop! []
  (when-let [system @*system]
    (component/stop-system system))
  (reset! *system nil))

(comment
  (start!)
  (stop!)
  @*system
  (get-in @*system [:json-datasource :datasource])
  (test/response-for
   (get-in @*system [:pedestal :server])
   :get "/api/indicators/id"))