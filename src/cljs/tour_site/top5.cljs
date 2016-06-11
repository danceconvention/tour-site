(ns tour-site.top5
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]))

(def entries (reagent/atom nil))

(defn leaderboard []
  (let [retrieve-leaderboard (fn [] (reset! entries ["one" "two" "three" "four"]))]
    (retrieve-leaderboard)
    (fn []
      [:ul.lead
       (for [entry @entries]
         ^{:key entry}
         [:li entry])])))
