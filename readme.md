# ProductLinRE

ProducLinRE is an online and open source platform for key functionalities in Requirments Engineering  of Software Product Lines, especially product planning.  It provides a repository  of artifacts which are used in development of features within a product line and facilitates the application engineering process by automatic selection of relevant artifacts to each product.  Users can only generate the appropriate artifacts and documentation necessary to communicate with product developers by selecting features for a particular product.
## Citation
please cite our paper if you use ProductLinRE in your work


@inproceedings{ghofrani2018productlinre,
  title={ProductlinRE: online management tool for requirements engineering of software product lines},
  author={Ghofrani, Javad and Fehlhaber, Anna Lena},
  booktitle={Proceeedings of the 22nd International Conference on Systems and Software Product Line-Volume 2},
  pages={17--22},
  year={2018},
  organization={ACM}
}
## Getting Started

### Prerequisites

To Build ProductLinRE you need:

- JDK 1.8 or newer
- Maven 3.3 or newer

To Run ProductLinRE you need:

- A server that supports Java servlets (i.e. Tomcat)
- A Postgresql 9.6.6 or newer Database (for other SQL databases add the driver dependency to the .pom)
- FFmpeg 3.4 or newer
- An e-mail account for sending registration e-mails

### Clone

Clone this repository to your local machine using `git clone <enter url here>`

### Build

To build ProductLinRe run `mvn package`. Make sure to edit the configuration file before building.

### Deployment

To deploy ProductLinRE you need to setup a http(s) server. We strongly recommend using https. Please refer to the server documentation for instructions. When the server is set up, copy the generated .war file to the appropriate directory and start the server.

### Configuration

To configure ProductLinRE you need to edit the `application.properties` file in `src/main/resources`. The most important properties are:

```
##### Admin information ########
setup.email=<admin email address>
setup.username=<admin username>
setup.password=<admin password for ProductLinRE> ### at least 8 characters

##### Email configuration ########
spring.mail.host=<url to smtp server>
spring.mail.port=<smtp server port>
spring.mail.username=<login user to smtp server>
spring.mail.password=<login password to smtp server>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

##### Default locale #####
spring.mvc.locale=<default locale tag>

##### Upload filesize limit #####
spring.http.multipart.max-file-size=<max filesize>
spring.http.multipart.max-request-size=<max filesize>

##### Path to FFMPEG executable ##### (required)
configdata.ffmpegPath=<path to ffmpeg executable>
configdata.ffprobePath=<path to ffprobe executable>

##### Daily upload limit per user #####
configdata.dailyUploadLimit=<daily upload limit>

##### Language tags of supported locales #####
configdata.supportedLanguages[0]=en
configdata.supportedLanguages[1]=de

##### Paths to used folders #####
configdata.welcomeTextPath=<path to welcome text, may contain html>
configdata.tosTextPath=<path to terms of service>
configdata.assetPath=<path to assets folder>
configdata.tempPath=<path to tmp folder>

##### Database configuration ##### (if missing, a H2 in memory database will be used)
spring.datasource.username=<database username>
spring.datasource.password=<database password>
spring.datasource.url=<database url, i.e. jdbc:postgresql://localhost:5432/databasePLRE2>
```

### Localization

To add support for additional locales, you need to add the language tag to `configdata.supportedLanguages` and add a file with translated message strings in 'messages.properties' as `messages_<locale_tag>.properties` to the resources folder. 

## License

ProductLinRe is licensed under the **[MIT license](LICENSE)**

Copyright 2018 © Fachgebiet Software Engineering, Leibniz Universität Hannover
