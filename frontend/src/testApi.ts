export async function testBackend() {
    const response = await fetch("/api/jobs");
    if (!response.ok) throw new Error("Backend not reachable");
    return await response.json();
}