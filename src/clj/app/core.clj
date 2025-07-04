(ns app.core
  (:require [com.stuartsierra.component :as component]
            [app.system.core :refer [new-system]])
  (:gen-class))

(defn -main []
  (-> (new-system)
      (component/start-system)))