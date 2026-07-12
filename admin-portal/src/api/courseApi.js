import axios from "axios";

const BASE_URL = "http://localhost:8080/api/courses";

export const getCourses = () => axios.get(BASE_URL);
export const addCourse = (course) => axios.post(BASE_URL, course);
export const updateCourse = (id, course) => axios.put(`${BASE_URL}/${id}`, course);
export const deleteCourse = (id) => axios.delete(`${BASE_URL}/${id}`);