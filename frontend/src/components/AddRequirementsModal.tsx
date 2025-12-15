import { useState } from "react";
import styles from "./ApplyModal.module.css"; // ← folosești CSS-ul modalului tău

interface AddRequirementsModalProps {
    visible: boolean;
    onClose: () => void;
    onSubmit: (form: RequirementsForm) => void;
}

interface RequirementsForm {
    minExperience: number;
    mustHaveSkills: string[];
    niceToHaveSkills: string[];
    softSkills: string[];
    seniority: string;
}

export default function AddRequirementsModal({
                                                 visible,
                                                 onClose,
                                                 onSubmit
                                             }: AddRequirementsModalProps) {

    const [minExperience, setMinExperience] = useState(0);
    const [mustHaveSkills, setMustHaveSkills] = useState("");
    const [niceToHaveSkills, setNiceToHaveSkills] = useState("");
    const [softSkills, setSoftSkills] = useState("");
    const [seniority, setSeniority] = useState("");

    if (!visible) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <h2 className={styles.modalTitle}>Add Requirements</h2>

                {/* Min Experience */}
                <label className={styles.fileLabel}>
                    Minimum Experience (years)
                    <input
                        type="number"
                        value={minExperience}
                        onChange={(e) => setMinExperience(Number(e.target.value))}
                    />
                </label>

                {/* Must-have skills */}
                <label className={styles.fileLabel}>
                    Must-have Skills
                    <input
                        type="text"
                        placeholder="React, Java, SQL"
                        value={mustHaveSkills}
                        onChange={(e) => setMustHaveSkills(e.target.value)}
                    />
                </label>

                {/* Nice-to-have skills */}
                <label className={styles.fileLabel}>
                    Nice-to-have Skills
                    <input
                        type="text"
                        placeholder="Docker, AWS..."
                        value={niceToHaveSkills}
                        onChange={(e) => setNiceToHaveSkills(e.target.value)}
                    />
                </label>

                {/* Soft skills */}
                <label className={styles.fileLabel}>
                    Soft Skills
                    <input
                        type="text"
                        placeholder="Teamwork, Communication..."
                        value={softSkills}
                        onChange={(e) => setSoftSkills(e.target.value)}
                    />
                </label>

                {/* Seniority */}
                <label className={styles.fileLabel}>
                    Seniority
                    <input
                        type="text"
                        placeholder="Junior, Mid, Senior..."
                        value={seniority}
                        onChange={(e) => setSeniority(e.target.value)}
                    />
                </label>

                <div className={styles.modalActions}>
                    <button
                        className={styles.secondaryBtn}
                        onClick={onClose}
                    >
                        Cancel
                    </button>

                    <button
                        className={styles.primaryBtn}
                        onClick={() => {
                            onSubmit({
                                minExperience,
                                mustHaveSkills: mustHaveSkills.split(",").map(s => s.trim()).filter(x => x),
                                niceToHaveSkills: niceToHaveSkills.split(",").map(s => s.trim()).filter(x => x),
                                softSkills: softSkills.split(",").map(s => s.trim()).filter(x => x),
                                seniority
                            });
                        }}
                    >
                        Save
                    </button>
                </div>
            </div>
        </div>
    );
}
