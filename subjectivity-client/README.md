# SubjectivityClient

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 13.1.2.

To start the web application there are two methods: running it on your local machine or running it in a Docker container.

## Running the Application Locally

### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

### Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Running the Application in a Docker Container
To run the application in a Docker container, follow these steps:

1. Build the docker image: run `docker build -t agile_8664 .`
2. Start the containers: run `docker run -v ${PWD}:/app -v /app/node_modules -p 4200:4200 --rm agile_8664`
    - With arguments :
        - `-v`: mount the angular application config files
        - `-p`: expose on your local machine the port 4200 that will be linked to the container port 4200 
        - `--rm`: delete all container and volumes after the container exits.
3. Connect to the web application: http://localhost:4200
