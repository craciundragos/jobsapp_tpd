export async function openResume(file: string) {
    const token = sessionStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/resume/${file}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error("Unauthorized problema problema");
    }

    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    window.open(url, "_blank");
}