(ns tour-site.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [goog.string :as gstring]
              [cljsjs.jquery]
              [tour-site.top5 :as top5]))

;; -------------------------
;; Views

(defn site-layout [contents]
  [:div
   [:nav.navbar.navbar-inverse.navbar-fixed-top
    [:div.container
     [:div.navbar-header
      [:a.navbar-brand {:href "#"}
       [:nobr
       [:img {:src "/img/dcnet_logo_header.png" :border 0}]
       (gstring/unescapeEntities "&nbsp;")
       [:strong "danceConvention.net"]]]
      ]
     [:div#navbar.navbar-collapse.collapse
      [:ul.nav.navbar-nav.navbar-right
       [:li [:a {:href "#"} "RawConnection Tour Logo"]]]]]]
   [:div.container
    [:div.jumbotron [:h1 "RawConnection Tour"]]
    [:div.row
     [:div.col-md-12 contents]]]
  ]
)

(defn home-page []
  (site-layout [:div.row
                [:div.col-md-6 [:h2 "Top 5"] [top5/leaderboard 10 5]]
                [:div.col-md-6 [:h2 "Search"]]
                ]))

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
