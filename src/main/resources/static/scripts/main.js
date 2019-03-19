/**
 * Created by Sigal on 5/21/2016.
 */
$(document).ready(function () {


    $(".restart-service-button").click(function () {
        var name = prompt("TYPE IN THE NAME OF THE SERVICE TO RESTART: ");
        if ($(this).attr("name") == name) {
            alert("yes");
            $.post("/restart-service.json?", "service=" + $(this).attr("id"), restartServiceResponse);
        } else {
            alert("no");
        }
    });


    function restartServiceResponse(data, status) {
        alert(data.error ? "Error! " : "Success!");
    }

    $(".ping-service-button").click(function () {
        $.post("/ping-service.json?", "service=" + $(this).attr("name"), pingServiceResponse);
    });

    function pingServiceResponse(data, status) {
        alert((data.error ? "Error! " : "Success!") + ", ping time: " + data.pingTime);
    }

    $(".toggle-job").click(function () {
        $.post("/toggle-job.json?", "name=" + $(this).attr("id"), toggleJobResponse);
    });

    function toggleJobResponse(data, status) {
        location.reload();
    }

    $("#updateAdminUserDetails").click(function () {
        var params = "";
        params += addParamValueFromHtml("oid");
        params += addParamValueFromHtml("name");
        params += addParamValueFromHtml("adminId");
        params += addParamValueFromHtml("phone");
        params += addParamValueFromHtml("email");
        params += addParamValueFromHtml("password");
        params += addParamValueFromHtml("businessName");
        params += addParamValueFromHtml("businessId");
        params += addParamValueFromHtml("maxUsers");
        params += addParamValueFromHtml("expirationDate");
        params += addParamValueFromHtml("mainOfficeAddress");
        params += addParamValueFromHtml("mainOfficePhone");
        params += addParamValueFromHtml("active");
        params += addParamValueFromHtml("allowExcelUpload");
        $.post("/update-admin-user.json?", params, generalResponse);

    });


    function addParamValueFromHtml(id) {
        var param = "";
        var value = $("#" + id).prop("type") == "checkbox" ? $("#" + id).prop("checked") : $("#" + id).val();
        if (!isUndefined(value)) {
            param = "&" + id + "=" + value;
        }
        return param;
    }

    function isUndefined(value) {
        return value == undefined || value == "undefined" || value === "undefined";
    }


    function getErrorMessage(code) {
        var message = "";
        switch (code) {
            case 2: {
                message = "מספר טלפון זה כבר קיים במערכת";
                break;
            }
            default: {
                message = "שגיאה כללית";
                break;
            }
        }
        return message;
    }

    $("#reloadAllVotersButton").click(function () {
        $.post("/reload-voters.json?", "", reloadVotersResponse);
    });

    function reloadVotersResponse() {
        alert("תהליך הטעינה החל")
    }

    $("input").change(function () {
        $("#addAdminUser").attr("disabled", false);
    });

    $("#addAdminUser").click(function () {
        var campaign = $('#campaigns').find(":selected").attr("oid");
        if (campaign > -1) {
            if (hasText($("#phone").val())) {
                if (hasText($("#name").val())) {
                    if (hasText($("#expirationDate").val())) {
                        $(this).attr("disabled", true);
                        var params = "";
                        params += addParamValueFromHtml("name");
                        params += addParamValueFromHtml("adminId");
                        params += addParamValueFromHtml("phone");
                        params += addParamValueFromHtml("email");
                        params += addParamValueFromHtml("password");
                        params += addParamValueFromHtml("businessName");
                        params += addParamValueFromHtml("businessId");
                        params += addParamValueFromHtml("maxUsers");
                        params += addParamValueFromHtml("expirationDate");
                        params += addParamValueFromHtml("mainOfficeAddress");
                        params += addParamValueFromHtml("mainOfficePhone");
                        params += addParamValueFromHtml("cost");
                        params += addParamValueFromHtml("totalSmsPurchased");
                        params += ("&campaignOid=" + campaign);
                        $.post("/save-new-admin.json?", params, generalRedirectResponse);
                    } else {
                        alert("חובה להכניס תאריך תוקף")

                    }
                } else {
                    alert("חובה לבחור שם למנהל הראשי")
                }
            } else {
                alert("חובה להכניס מספר טלפון")
            }
        } else {
            alert("בחר את הקמפיין הרצוי")
        }
    });

    function generalResponse(data, status) {
        if (data.error) {
            alert(getErrorMessage(data.code));
            $(this).attr("disabled", false);
        } else {
            alert("השינויים נשמרו בהצלחה");
        }

    }

    function hasText(text) {
        return !(isUndefined(text) || text == "" || text === "");
    }

    $("#reloadVotersButton").click(function () {
        var params = "adminOid=" + $(this).attr("oid");
        $.post("/reload-voters.json?", params, reloadVotersResponse);
    });

    $("#updateCustomPropertiesButton").click(function () {
        var params = "adminOid=" + $(this).attr("oid");
        params += ("&data=");
        $(".custom-property-checkbox").each((function () {
            if ($(this)[0].checked) {
                params += ($(this).attr("oid") + ",");
            }
        }));
        $.post("/map-custom-properties-to-admin.json?", params, generalResponse);
    });

    $("#campaignTypeSelect").change(function () {
        var id = $(this).find(":selected").attr("id");
        if (id == 1) {
            $("#cityRow").show();
        } else {
            $("#cityRow").hide();
        }
    });

    $("#addCampaign").click(function () {
        var params = "";
        var campaignType = $("#campaignTypeSelect").find(":selected").attr("id");
        params += addParamValueFromHtml("name");
        params += addParamValueFromHtml("date");
        params += ("&type=" + campaignType);
        var cityOid = $("#citySelect").find(":selected").attr("oid");
        params += ("&cityOid=" + cityOid);
        $.post("/save-new-campaign.json?", params, generalRedirectResponse);
    });

    function generalRedirectResponse(data, status) {
        if (data.error) {
            alert(getErrorMessage(data.code));
            $(this).attr("disabled", false);
        } else {
            alert("השינויים נשמרו בהצלחה");
            setTimeout(function () {
                window.location.href = data.redirect;
            }, 1500);
        }

    }

    $("#loadVotersBookButton").click(function () {
        var params = "campaignOid=" + $(this).attr("oid");
        $.post("/load-voters-book.json?", params, generalRedirectResponse);
    });

    $("#addConfigButton").click(function () {
        var params = "";
        params += addParamValueFromHtml("configKey");
        params += addParamValueFromHtml("configValue");
        $.post("/save-config.json?", params, generalRedirectResponse);
    });

    $(".update-config-button").click(function () {
        var oid = $(this).attr("oid");
        var value = $("#value" + oid).val();
        var key = $("#key" + oid).val();
        var params = "oid=" + oid + "&configValue=" + value + "&configKey=" + key;
        $.post("/update-config.json?", params, generalResponse);
    });

    $("#updateAdminScreensButton").click(function () {
        var params = "adminOid=" + $(this).attr("oid");
        params += ("&data=");
        $(".admin-screen-checkbox").each((function () {
            if ($(this)[0].checked) {
                params += ($(this).attr("oid") + ",");
            }
        }));
        $.post("/map-admin-screens.json?", params, generalResponse);
    });

    $("#syncWithVotersBookButton").click(function () {
        var oid = $(this).attr("oid");
        var params = "adminOid=" + oid;
        $.post("/sync-admin-with-voters-book.json?", params, reloadVotersResponse);
    });

    $("#loginBtn").click(function () {//the login button
        var params = "phone=" + $("#phone").val() + "&password=" + $("#password").val();
        $.post("/login-params?", params, loginResponse);
    });

    $("#forgotBtn").click(function () {
        alert("Availible soon");
    })


    function loginResponse(data) {
        if (data.success) {
            window.location.href = '/dashboard';

        } else {
            $("#loginError").text("NOT GOOD");
        }
    }

    $("#backgroundImg").attr('src',"..\images\image\background.jpg");


});

