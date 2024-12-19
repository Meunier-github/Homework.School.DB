-- liquibase formatted sql

-- changeset user1:1
CREATE INDEX student_name_index ON student (name);

-- changeset user1:2
CREATE INDEX faculty_name_color_index ON faculty (name, color);