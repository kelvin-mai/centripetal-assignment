(ns app.routes
  (:require [cheshire.core :as json]))

(defn response
  ([status]
   (response status nil))
  ([status body]
   (merge {:status status
           :headers {"Content-Type" "application/json"}}
          (when body {:body (json/encode body)}))))

(def ok (partial response 200))
(def created (partial response 201))
(def not-found (partial response 404))

(defn respond-hello [request]
  (ok {:hello "world"}))

(def routes
  #{["/hello" :get respond-hello :route-name :hello]})