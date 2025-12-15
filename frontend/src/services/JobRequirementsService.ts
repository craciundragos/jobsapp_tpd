export interface JobRequirementsForm {
    minExperience: number;
    mustHaveSkills: string[];
    niceToHaveSkills: string[];
    softSkills: string[];
    seniority: string;
}

export async function saveJobRequirements(jobId: number, form: JobRequirementsForm) {
    const token = sessionStorage.getItem("token");

    if (!token) {
        throw new Error("User not authenticated");
    }

    const response = await fetch(`/api/jobs/${jobId}/requirements`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify(form)
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || "Failed to save requirements");
    }

    return await response.json().catch(() => ({}));
}
