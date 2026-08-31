// Parameterised CI for the Libercus Playwright suite.
// Mirrors the Selenium project's env-driven execution (browser, tags, env) but uses
// Playwright projects/grep/shard. Credentials come from the Jenkins credential store.
properties([
    parameters([
        choice(name: 'project', choices: ['cms', 'pf3', 'regression'], description: 'Playwright project'),
        string(name: 'grep', defaultValue: '@smoke', description: 'Tag/title filter (Playwright --grep)'),
        string(name: 'shard', defaultValue: '1/1', description: 'Shard, e.g. 1/4'),
        choice(name: 'projectEnv', choices: ['TB', 'PG'], description: 'Customer env (PROJECT_ENV)'),
    ])
])

pipeline {
    agent any

    environment {
        PROJECT_ENV       = "${params.projectEnv}"
        HEADLESS          = 'true'
        CI                = 'true'
        // Injected from Jenkins credentials — never hardcode (Selenium committed these
        // in LoginDetails.yml; we do not).
        LIBERCUS_USERNAME = credentials('libercus-username')
        LIBERCUS_PASSWORD = credentials('libercus-password')
    }

    stages {
        stage('Install') {
            steps {
                sh 'npm ci'
                sh 'npx playwright install --with-deps chromium'
            }
        }
        stage('Typecheck') {
            steps {
                sh 'npx tsc --noEmit'
            }
        }
        stage('Test') {
            steps {
                sh """
                    npx playwright test \
                      --project=${params.project} \
                      --grep='${params.grep}' \
                      --shard=${params.shard}
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'playwright-report/**, test-results/**', allowEmptyArchive: true
            publishHTML([
                reportDir: 'playwright-report',
                reportFiles: 'index.html',
                reportName: 'Playwright Report',
                allowMissing: true,
                keepAll: true,
                alwaysLinkToLastBuild: true
            ])
        }
    }
}
