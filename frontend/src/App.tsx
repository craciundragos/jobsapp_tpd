import './App.css'
import { testBackend } from './testApi';
import { useEffect, useState } from 'react';

function App() {
    const [data, setData] = useState<any>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        testBackend()
            .then(setData)
            .catch((err) => setError(err.message));
    }, []);

    return (
        <div style={{ padding: 40 }}>
            <h1>JobsApp Frontend</h1>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {data ? (
                <pre>{JSON.stringify(data, null, 2)}</pre>
            ) : (
                <p>🔄 Fetching data from backend...</p>
            )}
        </div>
    );
}

export default App
