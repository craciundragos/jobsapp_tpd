import {API_BASE_URL} from "./api.ts";
export async function testBackend() {
    const response = await fetch(`${API_BASE_URL}/jobs`);
    if (!response.ok) throw new Error("Backend not reachable");
    return await response.json();
}