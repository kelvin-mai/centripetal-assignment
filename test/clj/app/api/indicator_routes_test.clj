(ns app.api.indicator-routes-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [cheshire.core :as json]
            [io.pedestal.connector.test :as test]
            [app.utils.fixtures :refer [with-system test-system]]))

(use-fixtures :once with-system)

(deftest indicator-routes-test
  (let [server (get-in @test-system [:pedestal :server])
        json-datasource (-> (slurp (io/resource "indicators.json"))
                            (json/decode true))]
    (testing "get all indicators"
      (let [response (test/response-for server :get "/api/indicators")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               json-datasource))))
    (testing "get all indicators with type ipv4"
      (let [response (test/response-for server :get "/api/indicators?type=ipv4")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (filter #(some (fn [i] (= (str/lower-case (:type i))
                                         "ipv4"))
                              (:indicators %))
                       json-datasource)))
        (is (= (count (json/decode (:body response) true)) 69))))
    (testing "get all indicators with type url"
      (let [response (test/response-for server :get "/api/indicators?type=url")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (filter #(some (fn [i] (= (str/lower-case (:type i))
                                         "url"))
                              (:indicators %))
                       json-datasource)))
        (is (= (count (json/decode (:body response) true)) 26))))
    (testing "get indicator by id successfully"
      (let [id (:id (first json-datasource))
            response (test/response-for server :get (str "/api/indicators/" id))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (first json-datasource)))))
    (testing "get indicator by id does not exist"
      (let [response (test/response-for server :get (str "/api/indicators/" "fake-id"))]
        (is (= (:status response) 404))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               nil))))))

(run-tests)