export let $copy = (text: string): Promise<void> => {
    const modernCopy = (content: string): Promise<void> => {
        return navigator.clipboard.writeText(content);
    };

    const fallbackCopy = (content: string): Promise<void> => {
        return new Promise((resolve, reject) => {
            const textArea = document.createElement("textarea");
            textArea.value = content;
            Object.assign(textArea.style, {
                position: "fixed",
                left: "-9999px",
                top: "0"
            });
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            try {
                document.execCommand('copy') ? resolve() : reject('ExecCommand failed');
            } catch (err) {
                reject(err);
            } finally {
                document.body.removeChild(textArea);
            }
        });
    };

    $copy = (navigator.clipboard && window.isSecureContext) ? modernCopy : fallbackCopy;

    return $copy(text);
};