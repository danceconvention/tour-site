(ns tour-site.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [tour-site.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app.container
      [:h3 "Loading Competition Tour..."]
      [:p "If this message doesn't disappear, please enable JavaScript in your browser."]])

(defn head []
  [:head
   [:title "Raw Connection Competition Tour"]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"       :content "width=device-width, initial-scale=1"}]
   [:meta {:name "og:url"         :content "http://rawconnection-tour.danceconvention.net/"}]
   [:meta {:name "og:title"       :content "Raw Connection Competition Tour"}]
   [:meta {:name "og:description" :content "The Raw Con Comp Tour is designed to encourage and reward dancers of all levels to compete at Raw Con Events"}]
   [:meta {:name "og:image"       :content "http://rawconnection-tour.danceconvention.net/img/rawcontour.jpg"}]
   (include-css
     (if (env :dev) "/css/site.css" "/css/site.min.css")
     (if (env :dev) "/bootstrap-3.3.6/css/bootstrap.css" "/bootstrap-3.3.6/css/bootstrap.min.css"))
   ])

(def loading-page
  (html5
    (head)
    [:body {:style "padding-top: 80px;"}
     mount-target
     (include-js "/js/app.js")
     (include-js
       (if (env :dev) "/bootstrap-3.3.6/js/bootstrap.js" "/bootstrap-3.3.6/js/bootstrap.min.js"))
     ]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/dancerinfo/:userid" [userid] loading-page)
  (GET "/about" [] loading-page)
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
