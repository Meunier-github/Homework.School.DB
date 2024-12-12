select s.name, s.age, f.name
from student as s
left join faculty as f on s.faculty_id = f.id;

select s.name, s.age
from student as s
inner join avatar as a on s.id = a.student_id;
