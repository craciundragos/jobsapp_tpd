import {API_BASE_URL} from "../api.ts";

export async function loginUser(email: string, password: string) {
    const response = await fetch(`${API_BASE_URL}/users/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
    });
    if (!response.ok) {
        const errorData = await response.json();
        console.log("Backend error:", errorData);
        throw errorData;
    }
    const data =await response.json();
    sessionStorage.setItem("token", data.token);

}

export async function registerUser(
    username: string,
    email: string,
    password: string,
    role: number
) {
    const response = await fetch(`${API_BASE_URL}/users/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password, role }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        console.log("Backend error (register):", errorData);
        throw errorData;
    }

    return await response.json();
}
export const logout = () => {
    sessionStorage.removeItem("token");
};


export function getRole(): string | null {
    const token = sessionStorage.getItem("token");
    if (!token) return null;

    try {
        const payloadBase64 = token.split(".")[1];
        const payloadJson = atob(payloadBase64);
        const payload = JSON.parse(payloadJson);
        return payload.role ?? null;
    } catch {
        return null;
    }
}

export function hasRole(role: "RECRUITER" | "CANDIDATE"): boolean {
    return getRole() === role;
}