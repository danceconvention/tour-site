(ns tour-site.server
  (:require [tour-site.handler :refer [app]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (env :port) "80"))]
     (run-jetty app {:port port :join? false})))
