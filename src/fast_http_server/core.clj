(ns fast-http-server.core
  (:require [org.httpkit.server :as server]
            [cheshire.core :refer [parse-string generate-string]]
            [babashka.cli :as cli]))

(def draft-id "some-draft-id")

(defn app [{:keys [:request-method :uri] :as req}]
  (case [request-method uri]
    [:post "/api/v1/model-replacement"]
    {:body (generate-string {:draftId draft-id})
     :headers {"Content-Type" "application/json"}
     :status 200}
    [:get "/foo"] {:body "Foo!"
                   :status 200}))

(defn serve
  [{:keys [port] :as opts}]
  (let []
    (binding [*out* *err*]
      (println (str "Server running  at http://localhost:" port)))
    (server/run-server app opts)))

(defn exec
  "Exec function, intended for command line usage. Same API as `serve` but
  blocks until process receives SIGINT."
  [opts]
    (do (serve opts)
        @(promise)))

(def ^:private cli-opts 
  {:spec   
   {:port {:desc "Desired port"
           :default 8090
           :coerce :long}}})


(comment 

(defn -main [& args]
  (exec (cli/parse-opts args cli-opts)))

(def args [])
(def my-server (serve (cli/parse-opts args cli-opts)))

(my-server)


)
