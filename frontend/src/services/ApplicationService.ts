export async function getApplications(jobId: number) {
    const token = sessionStorage.getItem("token");

    const response = await fetch(`/api/applications/job/${jobId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error("Eroare la preluarea aplicatiilor");
    }

    return await response.json();
}