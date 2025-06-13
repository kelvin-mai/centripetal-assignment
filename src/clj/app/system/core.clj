(ns app.system.core
  (:require [com.stuartsierra.component :as component]
            [aero.core :as aero]
            [clojure.java.io :as io]
            [app.system.json-datasource :refer [new-json-datasource]]
            [app.system.pedestal :refer [new-pedestal]]))

(defn new-system []
  (component/system-map
   :config (aero/read-config (io/resource "config.edn"))
   :json-datasource (component/using (new-json-datasource) [:config])
   :pedestal (component/using (new-pedestal) [:config :json-datasource])))