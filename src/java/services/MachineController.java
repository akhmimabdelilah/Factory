package services;

import entities.Machine;
import services.util.JsfUtil;
import services.util.JsfUtil.PersistAction;
import domaines.MachineFacade;
import entities.Employe;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@SessionScoped
@ManagedBean(name = "machineController")
public class MachineController implements Serializable {

    @EJB
    private domaines.MachineFacade ejbFacade;
    @EJB
    private domaines.EmployeFacade ejbFacadeEmp;

    private List<Machine> items = null;
    private List<Employe> employes = null;
//    private List<Object[]> nbMachine();

    private Employe employe;
    private Machine selected;
    private ChartModel barModel;

    public ChartModel getBarModel() {
        return barModel;
    }

    public void onEdit(RowEditEvent event) {
        selected = (Machine) event.getObject();
        selected.setEmploye(null);
        ejbFacade.edit(selected);
    }

    public void onCancel(RowEditEvent event) {
    }

    public MachineController() {

    }

    public List<Employe> getEmployes() {
        if (employe == null) {
            employe = selected.getEmploye();
        }
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public ChartModel createBarModel() {
        CartesianChartModel model = new CartesianChartModel();
        ChartSeries employe = new ChartSeries();
        employe.setLabel("employes");
        model.setAnimate(true);
//        for (Object[] e : ejbFacade.nbEmploye()) {
//            employe.set(e[1], Integer.parseInt(e[0].toString()));
//        }

        for (Machine m : ejbFacade.findAll()) {
            employe.set(m.getEmploye().getNom(), m.getEmploye().getMachineCollection().size());
        }

        model.addSeries(employe);
        model.setTitle("Zoom for Details");
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2024-02-01");
        axis.setTickFormat("%b %#d, %y");
        return model;

    }

    public ChartModel createBarYear() {
        LineChartModel model = new LineChartModel();
        LineChartSeries machine = new LineChartSeries();

        machine.setLabel("machines");
//        machine.setFill(true);
        model.setAnimate(true);

        // Create a map to keep track of the number of machines acquired by year
//        Map<Integer, Integer> machinesByYear = new HashMap<>();
        // Iterate over the machines
//        for (Machine m : ejbFacade.findAll()) {
//            int year = m.getDateAchat().getYear();
//
//            // Increment the count for the corresponding year in the map
//            machinesByYear.put(year, machinesByYear.getOrDefault(year, 0) + 1);
//
//        }
//        // Add the data to the machine ChartSeries
//        for (Map.Entry<Integer, Integer> entry : machinesByYear.entrySet()) {
//            for (Machine m : ejbFacade.findAll()) {
//                machine.set(m.getMarque(), entry.getValue());
//
//            }
////            machine.set(entry.getKey().toString(), entry.getValue());
//        }
        for (Machine m : ejbFacade.findAll()) {
//            machine.set(m.getDateAchat().getYear().toString(), m.getId());
            machine.set(m.getDateAchat(), m.getEmploye().getMachineCollection().size());

        }

        model.addSeries(machine);
        model.setTitle("Zoom for Details");
        model.setZoom(true);
        model.setSeriesColors("66cc66,ffffff,E7E658,cc6666");

        model.setTitle("Area Chart");
        model.setLegendPosition("ne");
        model.setStacked(true);
        model.setShowPointLabels(true);

        Axis xAxis = new CategoryAxis("Years");
        model.getAxes().put(AxisType.X, xAxis);
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Dates");
        yAxis.setMin(0);
        yAxis.setMax(300);

        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2024-02-01");
        axis.setTickFormat("%b %#d, %y");

        model.getAxes().put(AxisType.X, axis);

        return model;

    }

    public ChartModel createChartModel() {
        LineChartModel model = new LineChartModel();
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 90);
        girls.set("2008", 120);

        model.addSeries(boys);
        model.addSeries(girls);

        model.setTitle("Linear Chart");
        model.setLegendPosition("e");
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);

        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);

        model.setTitle("Zoom");
//        model.setZoom(true);
        model.setLegendPosition("e");
        yAxis = model.getAxis(AxisType.Y);

        yAxis.setMin(0);
        yAxis.setMax(10);

        return model;
    }

    public Machine getSelected() {
        return selected;
    }

    public void setSelected(Machine selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MachineFacade getFacade() {
        return ejbFacade;
    }

    public Machine prepareCreate() {
        selected = new Machine();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MachineCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MachineUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MachineDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Machine> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;

    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Machine> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Machine> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = Machine.class)
    public static class MachineControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MachineController controller = (MachineController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "machineController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Machine) {
                Machine o = (Machine) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Machine.class.getName()});
                return null;
            }
        }

    }

}
