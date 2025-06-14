(ns app.api.health-routes-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [io.pedestal.connector.test :as test]
            [app.utils.fixtures :refer [with-system test-system]]))

(use-fixtures :once with-system)

(deftest health-routes-test
  (testing "health route"
    (let [server (get-in @test-system [:pedestal :server])
          response (test/response-for server :get "/api/health-check")]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers :content-type]) "application/json"))
      (is (= (json/decode (:body response) true)
             {:hello "world"})))))