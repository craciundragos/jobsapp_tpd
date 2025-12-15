    import { Routes, Route } from "react-router-dom";
    import LoginPage from "../pages/LoginPage/LoginPage.tsx";
    import RegisterPage from "../pages/RegisterPage/RegisterPage.tsx";
    import HomePage from "../pages/HomePage/HomePage.tsx";
    import CompanyPage from "../pages/CompanyPage/CompanyPage.tsx";
    import ApplicationsPage from "../pages/ApplicationsPage/ApplicationsPage.tsx";

    function AppRouter() {
        return (
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/home" element={<HomePage />} />
                <Route path ="/jobs" element ={<CompanyPage/>} />
                <Route path="/applications/:jobId" element={<ApplicationsPage />} />
            </Routes>
        );
    }

    export default AppRouter;
