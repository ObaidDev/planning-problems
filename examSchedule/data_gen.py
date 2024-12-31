import uuid
import random
from datetime import datetime, timedelta
import json

# Defined course types as per your enum
COURSE_TYPES = ["MATHEMATICS", "PHYSICS", "CHEMSITRY", "BIOLOGY", "COMPUTER_SCIENCE"]

def generate_exam_schedule_data_with_students():
    num_rooms = 5
    num_time_slots = 8
    num_courses = 10
    num_students = 50
    num_exams = 10

    # Generate random time slots
    def random_time_slots():
        start = datetime(2024, 12, 31, 8, 0)
        slots = []
        for i in range(num_time_slots):
            end = start + timedelta(hours=2)
            slots.append({
                "id": str(uuid.uuid4()),
                "startTime": start.isoformat(),
                "endTime": end.isoformat()
            })
            start = end + timedelta(minutes=30)  # 30-minute break between slots
        return slots

    # Generate random courses
    def random_courses():
        courses = []
        for _ in range(num_courses):
            course_id = str(uuid.uuid4())
            courses.append({
                "id": course_id,
                "coursType": random.choice(COURSE_TYPES),
                "numberOfStudents": 0  # will be updated later
            })
        return courses

    # Generate random students
    def random_students(courses):
        students = []
        for _ in range(num_students):
            student_id = str(uuid.uuid4())
            enrolled_courses = random.sample(courses, random.randint(1, 3))  # Randomly assign 1 to 3 courses
            student = {
                "id": student_id,
                "name": f"Student_{student_id[:8]}",
                "coursesEnrolled": [{"id": course["id"], "coursType": course["coursType"], "numberOfStudents": course["numberOfStudents"]} for course in enrolled_courses]
            }
            students.append(student)
            # Update course's number of students
            for course in enrolled_courses:
                course["numberOfStudents"] += 1
        return students

    # Generate random exams
    def random_exams(courses, rooms, time_slots, students):
        exams = []
        for _ in range(num_exams):
            course = random.choice(courses)
            room = random.choice(rooms)
            time_slot = random.choice(time_slots)
            enrolled_students = random.sample(students, random.randint(1, course["numberOfStudents"]))  # Randomly select students for the exam

            # Add exam with students enrolled
            exam = {
                "id": str(uuid.uuid4()),
                "course": {
                    "id": course["id"],
                    "coursType": course["coursType"],
                    "numberOfStudents": course["numberOfStudents"],
                    "studentsEnrolled": enrolled_students  # List of students enrolled in this course for the exam
                },
                "room": {
                    "id": room["id"],
                    "name": room["name"],
                    "capacity": room["capacity"]
                },
                "timeSlot": time_slot
            }
            exams.append(exam)
        return exams

    # Random room data
    rooms = [{"id": str(uuid.uuid4()), "name": f"Room_{i+1}", "capacity": random.randint(20, 50)} for i in range(num_rooms)]

    # Random time slots and courses
    time_slots = random_time_slots()
    courses = random_courses()

    # Random students, and assign them to courses
    students = random_students(courses)

    # Generate random exams
    exams = random_exams(courses, rooms, time_slots, students)

    return {
        "rooms": rooms,
        "timeSlots": time_slots,
        "courses": courses,
        "exams": exams
    }

# Generate and save dataset
dataset = generate_exam_schedule_data_with_students()
output_path = "./data/EXAM_SCHEDULE_DUMMY_DATA.json"
with open(output_path, "w") as f:
    json.dump(dataset, f, indent=4)
print(f"Dataset saved to {output_path}")
