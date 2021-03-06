#!/usr/bin/env groovy

def call(Map param){
	def containerAgent = libraryResource 'delivery.sh'
	pipeline {
		agent {label "babu1"}
		stages {
			stage('Build') {
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
			stage('Deliver') {
				steps {
					withEnv([
						'ip='+param.ip
					]){
						sh(containerAgent)
					}
				}
			}
		}
	}
}
