#!/usr/bin/env bb
(require
 '[babashka.tasks :as tasks :refer [shell]]
 '[clojure.string :as string]
 '[clojure.tools.cli :refer [parse-opts]])

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

(defn install! [version]
  (println (str "🚚 Installing Java Temurin v" version))
  (shell "apt update")
  (shell (str "apt install --yes temurin-" version "-jdk")))

(defn done! [version]
  (println (str "✅ Java Temurin v" version " has been installed")))

(def java-version-missing "The Java Version must be specified (-v, --version), and be either: 8, 11, 16+")

(defn validate-options
  [{:keys [options errors] :as opts}]
  (println opts)
  (let [errors (set errors)]
    (cond-> (into options {:abort?    false
                           :abort-msg "internal error"})
      (errors java-version-missing)
      (assoc :abort? true :abort-msg java-version-missing))))

(def cli-options
  [["-v" "--version VERSION" "Java Version"
    :missing  java-version-missing
    :parse-fn #(Integer/parseInt %)
    :validate [#(or (#{8 11} %) (<= 16 %))]]])

(let [{:keys [abort? abort-msg
              version]} (-> (parse-opts *command-line-args* cli-options)
                            (validate-options))]
  (if-not abort?
    (do (setup!)
        (install! version)
        (done! version)
        (System/exit 0))
    (do (println abort-msg)
        (System/exit 1))))