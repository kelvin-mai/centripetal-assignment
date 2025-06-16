(ns app.api.response
  (:require [cheshire.core :as json]
            [malli.core :as m]
            [malli.error :as me]))

(defn response
  ([status]
   (response status nil))
  ([status body]
   (merge {:status status
           :headers {"Content-Type" "application/json"}}
          (when body {:body (json/encode body)}))))

(def ok (partial response 200))
(def created (partial response 201))
(def accepted (partial response 202))
(def not-found (partial response 404))

(defn malli-error [schema body]
  (response 400
            (-> (m/explain schema body)
                (me/humanize))))