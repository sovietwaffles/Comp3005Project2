INSERT INTO Users (email,pwd,rolle,first_name, last_name,amount_owed) VALUES
('test@test.test','password','admin','Ethan','Ross','0'),
('trainer@email.com','password','trainer','Generic','Trainer','0'),
('johnsmith@email.com','password','member','John','Smith','0');

INSERT INTO TrainerSessions(trainer_email,session_day,start_time,end_time,student_email,fee) VALUES
('trainer@email.com','2024-04-17','8:15:00','8:45:00','johnsmith@email.com','27.50'),
('trainer@email.com','2024-04-17','9:15:00','9:45:00','','27.50');


INSERT INTO GroupClasses(title,trainer_email,class_day,start_time,end_time,max_capacity,students,student_emails,room,fee) VALUES
('Zesty? Cycling','trainer@email.com','2024-05-16','11:00:00','12:00:00',40,1,'{johnsmith@email.com}','Bike Room',47.50),
('Less Zesty Cycling','trainer@email.com','2024-05-16','1:00:00','2:00:00',40,0,'{}','Bike Room',50.00);

INSERT INTO exerciseRoutines(trainer_email,estimated_duration,equipment_needed,routine_text) VALUES
('trainer@email.com',30,'','Just do pushups for 30 minutes');

INSERT INTO equipment(serial_number,title,current_hours,maintenance_hours,val) VALUES
(17235645,'Treadmill',1004,1000,250),
(17645325,'Treadmill',12,1000,250);
