#!/usr/bin/env bb
(require
 '[babashka.tasks :as tasks :refer [shell]]
 '[clojure.string :as string])

(defn remove-trailing-linebreak
  "Returns the given string without a trailing \n is there was one."
  [s]
  {:pre [(string? s)]}
  (let [[_ cg] (re-matches #"^(.*)\n?$" s)]
    cg))

(def shell-str
  (partial tasks/shell {:out :string}))

(def shell-str-out
  (comp remove-trailing-linebreak :out shell-str))

(defn setup! []
  (println "🛠️ Setting Up Java Temurin Repository")
  (shell "mkdir -p /etc/apt/keyrings")
  (-> (shell-str "wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public")
      (shell "tee /etc/apt/keyrings/adoptium.asc"))
  (-> (shell-str (string/join
                  " "
                  ["echo deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb"
                   (shell-str-out "awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release")
                   "main"]))
      (shell "tee /etc/apt/sources.list.d/adoptium.list")))

(defn install! []
  (println "🚚 Installing Java Temurin")
  (shell "apt update")
  (shell "apt install --yes temurin-17-jdk"))

(defn done! []
  (println "✅ Java Temurin has been installed"))

(setup!)
(install!)
(done!)