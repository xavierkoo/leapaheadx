<h1 align="center"> LeapAheadX </h1> <br>

LeapAheadX is a form management system to digitalize the form management processes of the client company <a href="https://www.quantumleapinc.net/">Quantum Leap</a>.<br><br>
  Features include:<br>
- Versatile form fields: Nine different field types, including phone number, address, email, and checkboxes, making it easy to collect the data you need.
- Robust security: Secured with Spring Security authentication, ensuring that only authorized users can access sensitive information.
- Streamlined workflows: All-in-one dashboard provides easy access for admins and vendors to view, edit, and submit forms in a seamless workflow.
- User-friendly customization: Drag-and-drop workflow and form builders makes customization easy, allowing you to create workflows from created forms quickly.
- Efficient validation: Form validation and approval, with the added function of saving partially edited forms, streamlining your workflow.
- Automated emailing: Sends automatic email notifications for form submissions and reminders, making it easy to keep track of submissions.
- Automated scoring: For forms enabled with evaluation ratings, the system automatically calculates scores, saving you time and effort.
- PDF generation: Generate printouts in PDF format, making it easy to share information with others.

## Table of Contents

- [Tech Stack](#tech-stack)
- [UML Diagram](#uml-diagram)
- [Database Entity-Relationship Diagram](#database-entity-relationship-diagram)
- [Project Setup](#project-setup)

## Tech Stack
### Frontend
* Sass
* Bootstrap
* Vue.js

### Backend
* Spring Boot MVC
* MySQL

## UML Diagram
Click the image for the expanded view
<img width="1264" alt="Screenshot 2023-04-30 at 1 39 56 AM" src="https://user-images.githubusercontent.com/86020207/235316450-8d86f1bd-7c8e-4311-88f5-70d5ffec10f2.png">

## Database Entity-Relationship Diagram
Click the image for the expanded view
<img width="1009" alt="Screenshot 2023-04-30 at 1 39 39 AM" src="https://user-images.githubusercontent.com/86020207/235316446-17f02afb-4f62-4176-a98c-f60ec439d0d1.png">


## Project Setup
### Pre-requisites
- JDK 17+
- Maven 3+
- Docker
- MySQL
- Wamp
- Spring Boot 3.0
- Spring Security
- bcrypt

### Pre-installation
#### Backend (located in [leapaheadx-backend](https://github.com/xavierkoo/leapaheadx-frontend/tree/main/leapaheadx-backend) folder)
During development it is recommended to use the profile local. In IntelliJ, -Dspring.profiles.active=local can be added in the VM options of the Run Configuration after enabling this property in "Modify options". <br>
Update your local database connection in application.yml or create your own application-local.yml file to override settings for development.<br>
After starting the application it is accessible under localhost:8080.

### Installation
Clone the git repository:
```bash
$ git clone git@github.com:xavierkoo/leapaheadx-frontend.git
```

#### Frontend
#### Install dependencies via
```sh
npm install
```
#### Compile and Hot-Reload for Development

```sh
npm run dev
```
#### Compile and Minify for Production

```sh
npm run build
```

#### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```

#### Backend
Local:
1. Make sure MAMP/WAMP is on
2. Run create.sql
3. Compile and Run
```sh
build.sh
```
4. Run data_v2.sql

Docker:
1. Navigate to [leapaheadx-backend](https://github.com/xavierkoo/leapaheadx-frontend/tree/main/leapaheadx-backend) folder
2. 

![Screenshot 2023-04-03 at 10 53 17 PM](https://user-images.githubusercontent.com/86020207/229547673-480c09ea-4659-49d6-9eed-d285947fa6a7.png)
![Screenshot 2023-04-03 at 10 54 06 PM](https://user-images.githubusercontent.com/86020207/229547695-55644a40-753f-4a00-9f0e-221cdbcd5fba.png)
<img width="1680" alt="Screenshot 2023-04-29 at 1 35 27 AM" src="https://user-images.githubusercontent.com/86020207/235219769-a664a563-019b-432f-93ce-b43a918e8e2c.png">
