(ns tour-site.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [goog.string :as gstring]
              [cljsjs.jquery]
              [tour-site.top5 :as top5]
              [tour-site.search :as search]
              [tour-site.dancerinfo :as dancerinfo]))

;; -------------------------
;; Views

(defn site-layout [contents]
  [:div
   [:nav.navbar.navbar-inverse.navbar-fixed-top
    [:div.container
     [:div.navbar-header
      [:a.navbar-brand {:href "https://danceconvention.net/"}
       [:nobr
       [:img {:src "/img/dcnet_logo_header.png" :border 0}]
       (gstring/unescapeEntities "&nbsp;")
       [:strong "danceConvention.net"]]]
      ]
     [:div#navbar.navbar-collapse.collapse
      [:ul.nav.navbar-nav.navbar-right
       [:li [:a.navbar-brand {:href "/"} [:strong "Raw Connection Competition Tour"]]]]]]]
   [:div.container
    [:div
     [:a.thumbnail {:href "/"} [:img {:src "/img/rawcontour.jpg" :border 0}]]]
    [:div.row
     [:div.col-md-12 contents]]
    [:footer {:style {:margin-top "40px"}} [:p [:small "Powered by danceConvention.net" (gstring/unescapeEntities "&copy;") "Ilya Obshadko"]]]]
  ]
)

(defn home-page []
  (site-layout [:div.row
                [:div.col-md-6 [:h2 "Top 5"] [top5/leaderboard 10 5]]
                [:div.col-md-6 [:h2 "Search"] [search/search-form]]
                ]))

(defn user-page []
  (site-layout [dancerinfo/dancer-info (session/get :current-user)]))

(defn about-page []
  (site-layout [:div [:h2 "About tour-site"]
               [:div [:a {:href "/"} "go to the home page"]]]))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/dancerinfo/:userid" [userid]
  (session/put! :current-user (js/parseInt userid))
  (session/put! :current-page #'user-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
