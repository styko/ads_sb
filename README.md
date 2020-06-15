# ads_sb

##### What is implemented
 - Custom JWT Token authorization and authentication with Spring Security
 - Multi stage docker build
 - Github action for simple CI
 - Deployment ready for dokku 
 - Spring data rest repo with security
 - Gmail API for getting only changes from realestate sites
 - Use of Pageobject pattern for Selenium running in docker
 - Webdriver switches base on config from local to remote mode

##### Sonar cloud

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=security_rating)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=sqale_index)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=code_smells)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=realestate_email_aggregator&metric=coverage)](https://sonarcloud.io/dashboard?id=realestate_email_aggregator)

##### Installation instructions
Get OAuth token for [GoogleAPI](https://developers.google.com/identity/protocols/oauth2/native-app).
Create tokens directory in project and copy credentials.json and change email account,app in dev config.
