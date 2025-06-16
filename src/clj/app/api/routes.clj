(ns app.api.routes
  (:require [app.api.response :refer [ok]]
            [app.api.indicators.handlers :as indicators]))

(defn health-check [_]
  (ok {:healthy true}))

(def routes
  (set (concat [["/api/health-check" :get health-check :route-name ::health-check]]
               indicators/routes)))