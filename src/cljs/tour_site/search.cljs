(ns tour-site.search
  (:require[secretary.core :as secretary :include-macros true]
           [accountant.core :as accountant]
           [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields init-field value-of]]
            [tour-site.appdb :refer [tourinfo-data]]))

(defn- navigate-to [userid]
  (accountant/navigate! (str "/dancerinfo/" userid)))

(defn user-lookup [text]
  (filter #(-> (:fullName %) (.toLowerCase %) (.indexOf text) (> -1))
          (sort-by :fullName (distinct (map #(into {} [[:userId    (:participantId %)]
                                                       [:fullName  (str (:firstName %) " " (:lastName %))]]) (tourinfo-data 10))))))

(defn search-form []
  (let [doc (reagent/atom {})]
    [:div {:style {:margin-top "40px"}}
      [bind-fields
       [:div {:field             :typeahead
              :id                :userlookup
              :input-placeholder "enter first or last name"
              :data-source       user-lookup
              :result-fn         #(:fullName %)
              :choice-fn         #(navigate-to (:userId %))
              :input-class       "input-lg form-control"
              :list-class        "typeahead-list"
              :item-class        "typeahead-item"
              :highlight-class   "highlighted"}]
       doc]]))
