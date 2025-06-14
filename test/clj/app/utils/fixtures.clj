(ns app.utils.fixtures
  (:require [com.stuartsierra.component :as component]
            [app.system.core :refer [new-system]]))

(def test-system (atom nil))

(defn with-system [f]
  (reset! test-system
          (component/start (new-system)))
  (f)
  (component/stop @test-system)
  (reset! test-system nil))