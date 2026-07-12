import { useState } from "react";
import axios from "axios";
import { addCourse } from "../api/courseApi";

const UPLOAD_URL = "http://localhost:8080/api/courses/upload-image";

export default function AddCourse() {
  const [courseName, setCourseName] = useState("");
  const [price, setPrice] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault(); // stop the browser's default page-reload form submit

    if (!imageFile) {
      setMessage("Please select an image.");
      return;
    }

    setSubmitting(true);
    setMessage("");

    try {
      // Step A: upload the image file first, get back the saved filename
      const formData = new FormData();
      formData.append("file", imageFile);

      const uploadRes = await axios.post(UPLOAD_URL, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      const savedFileName = uploadRes.data; // controller returns the filename as plain text

      // Step B: now create the course, using the filename we just got
      await addCourse({
        courseName: courseName,
        price: parseFloat(price),
        imageFileName: savedFileName,
        active: true,
      });

      setMessage("Course added successfully!");
      setCourseName("");
      setPrice("");
      setImageFile(null);
      e.target.reset(); // clears the file input visually
    } catch (err) {
      console.error("Failed to add course", err);
      setMessage("Something went wrong. Check console.");
    }

    setSubmitting(false);
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Add New Course</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "10px" }}>
          <label>Course Name: </label>
          <input
            type="text"
            value={courseName}
            onChange={(e) => setCourseName(e.target.value)}
            required
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Price: </label>
          <input
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Course Image: </label>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => setImageFile(e.target.files[0])}
            required
          />
        </div>

        <button type="submit" disabled={submitting}>
          {submitting ? "Adding..." : "Add Course"}
        </button>
      </form>

      {message && <p>{message}</p>}
    </div>
  );
}