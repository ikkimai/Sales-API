const saleForm = document.querySelector("#saleForm");
const summaryForm = document.querySelector("#summaryForm");
const saleMessage = document.querySelector("#saleMessage");
const summaryMessage = document.querySelector("#summaryMessage");
const summaryBody = document.querySelector("#summaryBody");
const apiStatus = document.querySelector("#apiStatus");

const today = new Date().toISOString().slice(0, 10);
saleForm.saleDate.value = today;
summaryForm.startDate.value = today;
summaryForm.endDate.value = today;

saleForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    setMessage(saleMessage, "");
    setBusy(saleForm, true, "Salvando");

    const payload = {
        saleDate: saleForm.saleDate.value,
        value: Number(saleForm.value.value),
        sellerId: Number(saleForm.sellerId.value),
        sellerName: saleForm.sellerName.value.trim()
    };

    try {
        const sale = await request("/sales", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });

        setMessage(saleMessage, `Venda #${sale.id} criada.`, "success");
        saleForm.value.value = "";
        saleForm.sellerId.value = "";
        saleForm.sellerName.value = "";
        await loadSummary();
    } catch (error) {
        setMessage(saleMessage, error.message, "error");
    } finally {
        setBusy(saleForm, false, "Pronta");
    }
});

summaryForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadSummary();
});

async function loadSummary() {
    setMessage(summaryMessage, "");
    setBusy(summaryForm, true, "Buscando");

    const params = new URLSearchParams({
        startDate: summaryForm.startDate.value,
        endDate: summaryForm.endDate.value
    });

    try {
        const sellers = await request(`/sellers?${params}`);
        renderSummary(sellers);
        setMessage(summaryMessage, sellers.length ? `${sellers.length} vendedor(es) encontrado(s).` : "Nenhuma venda no periodo.", "success");
    } catch (error) {
        setMessage(summaryMessage, error.message, "error");
    } finally {
        setBusy(summaryForm, false, "Pronta");
    }
}

async function request(url, options) {
    const response = await fetch(url, options);
    const body = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(body?.message || "Erro ao acessar a API.");
    }

    return body;
}

function renderSummary(sellers) {
    if (!sellers.length) {
        summaryBody.innerHTML = '<tr><td colspan="3" class="empty">Nenhuma venda no periodo.</td></tr>';
        return;
    }

    summaryBody.innerHTML = sellers.map((seller) => `
        <tr>
            <td>${escapeHtml(seller.sellerName)}</td>
            <td>${seller.totalSales}</td>
            <td>${seller.dailySalesAverage}</td>
        </tr>
    `).join("");
}

function setBusy(form, busy, label) {
    form.querySelector("button").disabled = busy;
    apiStatus.textContent = label;
}

function setMessage(element, message, type = "") {
    element.textContent = message;
    element.className = type ? `message ${type}` : "message";
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
