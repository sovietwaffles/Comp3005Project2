CREATE DATABASE Gym;

CREATE TABLE Users(
    email VARCHAR(255) UNIQUE Primary Key,
    pwd VARCHAR(255) NOT NULL,
    rolle VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    amount_owed FLOAT,
	goal VARCHAR(255),
	goal_metric FLOAT
);

CREATE TABLE TrainerSessions(
    session_id SERIAL PRIMARY KEY,
    trainer_email VARCHAR(255) NOT NULL,
    session_day DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    fee FLOAT,
    student_email VARCHAR(255)
);

CREATE TABLE GroupClasses(
	class_id SERIAL PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	trainer_email VARCHAR(255) NOT NULL,
	class_day DATE NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	max_capacity INTEGER,
	students INTEGER,
	student_emails text [],
	room VARCHAR(255),
	fee FLOAT
);

CREATE TABLE exerciseRoutines(
	routine_id SERIAL PRIMARY KEY,
	trainer_email VARCHAR(255),
	estimated_duration INTEGER,
	equipment_needed VARCHAR(255),
	routine_text VARCHAR(255) NOT NULL
);

CREATE TABLE equipment(
	serial_number INTEGER NOT NULL,
	title VARCHAR(255) NOT NULL,
	current_hours FLOAT,
	maintenance_hours FLOAT NOT NULL,
	val INTEGER
);
