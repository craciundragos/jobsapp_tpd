import {API_BASE_URL} from "../api.ts";
export async function getAllJobs() {
    const response = await fetch(`${API_BASE_URL}/jobs`);
    if (!response.ok) {
        throw new Error("Eroare la preluarea joburilor");
    }
    return await response.json();
}

export async function applyToJob(jobId: number, resumeFile: File) {
    const token = sessionStorage.getItem("token");
    console.log("Tokenul JWT:", token);
    if (!token) {
        throw new Error("User not authenticated");
    }
    const formData = new FormData();
    formData.append("jobId", String(jobId));
    formData.append("resume", resumeFile);

    const res = await fetch(`${API_BASE_URL}/applications/apply`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
        },
        body: formData,
    });
    if(res.status === 401){
        throw new Error("You need to be logged in as a CANDIDATE to apply for jobs.");
    }
    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.error);
    }

    return await res.json();
}

export async function getAllRecruiterJobs() {
    const token = sessionStorage.getItem("token");
    const response = await fetch(`${API_BASE_URL}/jobs/recruiter`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    });
    if (!response.ok) {
        throw new Error("Error fetching jobsr");
    }
    return await response.json();
}

export async function createJob(job: {
    title: string;
    description: string;
    companyId: number;
}) {
    const token = sessionStorage.getItem("token");

    const response = await fetch(`${API_BASE_URL}/jobs`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(job)
    });

    if (!response.ok) {
        throw new Error("Error creating job");
    }

    return await response.json();
}

    //public class ApplicationDTO {
//     private Integer id;
//     private Integer userId;
//     private Integer jobId;
//     private String resumeUrl;
//     private String status;
//     private BigDecimal rankAi;
// }