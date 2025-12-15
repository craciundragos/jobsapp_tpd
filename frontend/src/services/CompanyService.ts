export async function getRecruiterCompanies() {
    const token = sessionStorage.getItem("token");
    const response = await fetch("/api/companies", {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) throw new Error("Failed to fetch companies");

    return response.json();
}
export async function createCompany(name: string) {
    const token = sessionStorage.getItem("token");
    const response = await fetch("/api/companies", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name })
    });

    if (!response.ok) {
        throw new Error("Error creating job");
    }
    return await response.json();
}