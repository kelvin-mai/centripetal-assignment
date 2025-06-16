(ns app.api.indicators.datasource
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn find-by-id [ds id]
  (->> ds
       (filter #(= id (:id %)))
       (first)))

(defn find-docs-with-type [ds type]
  (filter #(some (fn [i] (str/includes?
                          (str/lower-case (:type i))
                          (str/lower-case type)))
                 (:indicators %))
          ds))

(defn docs-includes-string-kv [ds k v]
  (filter #(str/includes? (str/lower-case (get % k))
                          (str/lower-case v))
          ds))

(defn docs-includes-tags [ds v]
  (filter #(and (coll? v)
                (set/subset?
                 (set v)
                 (set (:tags %))))
          ds))