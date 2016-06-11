(ns tour-site.search)

(defn search-form []
  [:form.form {:style {:margin-top "40px"}}
   [:div.input-group
    [:input.form-control {:type "text" :placeholder "enter name"}]
    [:span.input-group-btn
     [:button.btn.btn-primary {:type "submit"} "Search"]]
    ]])