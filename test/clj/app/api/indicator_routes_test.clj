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

(deftest indicator-route-type-test
  (let [server (get-in @test-system [:pedestal :server])
        json-datasource (-> (slurp (io/resource "indicators.json"))
                            (json/decode true))]
    (testing "get all indicators with type ipv4"
      (let [response (test/response-for server :get "/api/indicators?type=ipv4")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (filter #(some (fn [i] (str/includes? (str/lower-case (:type i))
                                                     "ipv4"))
                              (:indicators %))
                       json-datasource)))
        (is (= (count (json/decode (:body response) true)) 69))))
    (testing "get all indicators with type url"
      (let [response (test/response-for server :get "/api/indicators?type=url")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (filter #(some (fn [i] (str/includes? (str/lower-case (:type i))
                                                     "url"))
                              (:indicators %))
                       json-datasource)))
        (is (= (count (json/decode (:body response) true)) 26))))
    (testing "get all indicators with type filehash"
      (let [response (test/response-for server :get "/api/indicators?type=filehash")]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (json/decode (:body response) true)
               (filter #(some (fn [i] (str/includes? (str/lower-case (:type i))
                                                     "filehash"))
                              (:indicators %))
                       json-datasource)))
        (is (= (count (json/decode (:body response) true)) 28))))))

(deftest indicator-route-search-test
  (let [server (get-in @test-system [:pedestal :server])
        search-body {:tlp "green"
                     :author_name "rio"
                     :name "logs"
                     :description "apache"
                     :tags ["honeypot" "exploits"]}]
    (testing "search indicators with no search body"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"})]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 100))))
    (testing "search indicators with full search body"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode search-body))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 7))))
    (testing "search indicators with searh param tlp"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode {:tlp "green"}))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 83))))
    (testing "search indicators with search param author_name"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode {:author_name "rio"}))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 36))))
    (testing "search indicators with search param name"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode {:name "logs"}))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 36))))
    (testing "search indicators with search param description"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode {:description "apache"}))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 7))))
    (testing "search indicators with search param tags"
      (let [response (test/response-for server
                                        :post "/api/indicators/search"
                                        :headers {:content-type "application/json"}
                                        :body (json/encode {:tags ["honeypot" "exploits"]}))]
        (is (= (:status response) 200))
        (is (= (get-in response [:headers :content-type]) "application/json"))
        (is (= (count (json/decode (:body response) true)) 7))))
      ;
    ))

(comment
  (run-tests)
  (json/encode {:tlp "green"
                :author_name "rio"
                :name "logs"
                :description "apache"
                :tags ["honeypot" "exploits"]}))
