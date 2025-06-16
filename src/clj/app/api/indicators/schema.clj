(ns app.api.indicators.schema
  (:require [malli.util :as mu]))

(def search-params
  (mu/optional-keys
   [:map {:open? true}
    [:tlp :string]
    [:author_name :string]
    [:name :string]
    [:description :string]
    [:tags [:vector :string]]]))